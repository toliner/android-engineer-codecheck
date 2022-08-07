/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * TwoFragment で使う
 */
class ListViewModel(
    private val repository: GitHubSearchRepository
) : ViewModel() {
    /**
     * States
     */
    private val _searchResults = MutableStateFlow<List<Item>>(emptyList())
    val searchResults: StateFlow<List<Item>>
        get() = _searchResults.asStateFlow()

    /**
     * Internal Variables
     */
    private var searchJob: Job? = null
    fun searchRepository(inputText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val searchResult = repository.searchRepository(inputText)
            _searchResults.update { searchResult }
        }
    }
}

class OneViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return ListViewModel(
                repository = GitHubSearchRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}