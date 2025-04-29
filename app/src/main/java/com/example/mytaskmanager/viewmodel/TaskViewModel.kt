package com.example.mytaskmanager.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytaskmanager.R
import com.example.mytaskmanager.model.Task
import com.example.mytaskmanager.repository.TaskRepository
import com.example.mytaskmanager.utils.AppConstants
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    // Original data source
    val allTasks: LiveData<List<Task>> = repository.allTasks

    // Filter parameters
    private val _searchQuery = MutableLiveData("")
    private val _selectedPriority = MutableLiveData<String?>(null)
    private val _sortOrder = MutableLiveData("asc")
    private val _sortBy = MutableLiveData("due_date")
    private val _fromDate = MutableLiveData<String?>(null)
    private val _toDate = MutableLiveData<String?>(null)
    private val _showCompleted = MutableLiveData(false)

    // Exposed LiveData
    val searchQuery: LiveData<String> = _searchQuery
    val selectedPriority: LiveData<String?> = _selectedPriority
    val sortOrder: LiveData<String> = _sortOrder
    val sortBy: LiveData<String> = _sortBy
    val fromDate: LiveData<String?> = _fromDate
    val toDate: LiveData<String?> = _toDate
    val showCompleted: LiveData<Boolean> = _showCompleted

    // Combined filtered result
    val filteredTasks: LiveData<List<Task>> = MediatorLiveData<List<Task>>().apply {
        val update: () -> Unit = { value = computeFiltered() }
        listOf(
            allTasks,
            _searchQuery,
            _selectedPriority,
            _sortOrder,
            _sortBy,
            _fromDate,
            _toDate,
            _showCompleted
        )
            .forEach { source -> addSource(source) { update() } }
    }

    // Date formatter
    private val formatter = DateTimeFormatter.ofPattern(AppConstants.YYYY_MM_DD, Locale.getDefault())

    // Task operations
    fun insertTask(task: Task) = viewModelScope.launch { repository.insertTask(task) }
    fun deleteTask(task: Task) = viewModelScope.launch { repository.deleteTask(task) }
    fun updateTask(task: Task) = viewModelScope.launch { repository.updateTask(task) }

    // Filter setters
    fun setSearchQuery(query: String) = _searchQuery.postValue(query)
    fun setFilterParameters(
        priority: String? = _selectedPriority.value,
        sortOrder: String = _sortOrder.value ?: "asc",
        sortBy: String = _sortBy.value ?: "due_date",
        fromDate: String? = _fromDate.value,
        toDate: String? = _toDate.value
    ) {
        _selectedPriority.value = priority
        _sortOrder.value = sortOrder
        _sortBy.value = sortBy
        _fromDate.value = fromDate
        _toDate.value = toDate
    }

    fun clearFilters(context: Context) =
        setFilterParameters(null, context.getString(R.string.asc), context.getString(R.string.due_date), null, null).also { _searchQuery.value = "" }

    fun setShowCompleted(show: Boolean) = _showCompleted.postValue(show)

    // Core filtering logic
    private fun computeFiltered(): List<Task> {
        val tasks = allTasks.value.orEmpty()
        val q = searchQuery.value.orEmpty().trim()
        val prio = selectedPriority.value
        val order = sortOrder.value ?: "asc"
        val by = sortBy.value ?: "due_date"
        val from = parseDate(fromDate.value)
        val to = parseDate(toDate.value)

        // build sequence of filters
        val seq = tasks.asSequence()
            .filter { it.isCompleted == showCompleted.value }
            .filter {
                q.isEmpty() || it.title.contains(q, true) || (it.description?.contains(
                    q,
                    true
                ) == true)
            }
            .filter { prio == null || it.priority.equals(prio, true) }
            .let { s ->
                if (from != null && to != null) {
                    s.filter { parseDate(it.dueDate)?.let { d -> !d.isBefore(from) && !d.isAfter(to) } == true }
                } else s
            }
            .toList()

        // sort
        return when (by) {
            "priority" -> sortByPriority(seq, order)
            "due_date" -> sortByDate(seq, { parseDate(it.dueDate) }, order)
            "created_date" -> sortByDate(seq, { parseDate(it.createdAt) }, order)
            else -> seq
        }
    }

    private fun parseDate(dateStr: String?): LocalDate? = try {
        dateStr?.let { LocalDate.parse(it, formatter) }
    } catch (e: DateTimeParseException) {
        null
    }

    private fun sortByPriority(list: List<Task>, order: String) = list.sortedWith(compareBy {
        when (it.priority.lowercase()) {
            "high" -> 1
            "medium" -> 2
            "low" -> 3
            else -> 4
        }
    }).let { if (order == "desc") it.reversed() else it }

    private fun sortByDate(
        list: List<Task>,
        selector: (Task) -> LocalDate?,
        order: String
    ) = list.sortedWith(compareBy(selector)).let { if (order == "desc") it.reversed() else it }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
