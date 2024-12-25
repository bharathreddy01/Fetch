package com.fetch.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fetch.models.Item
import com.fetch.repo.Repository
import kotlinx.coroutines.launch

class ItemViewModel(private val repository: Repository) : ViewModel() {
    private val _items = MutableLiveData<Map<Int, List<Item>>>()
    val items: LiveData<Map<Int, List<Item>>> get() = _items

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun fetchItems() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val fetchedItems = repository.getItems()
                val filteredAndGrouped =
                    fetchedItems.filter { it.name?.isNotBlank() == true } // Remove items with blank or null names
                        .groupBy { it.listId } // Group by listId
                        .toSortedMap() // Sort groups by listId (ascending)
                        .mapValues { entry -> entry.value.sortedBy { it.name } } // Sort items in each group by name
                _items.value = filteredAndGrouped
            } catch (e: Exception) {
                // Handle error if needed
            } finally {
                _loading.value = false
            }
        }
    }
}