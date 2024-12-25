package com.fetch.composobles

import org.junit.jupiter.api.Assertions.*

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fetch.models.Item
import com.fetch.repo.Repository
import com.fetch.viewModel.ItemViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@OptIn(ExperimentalCoroutinesApi::class)
class ItemViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository = mockk<Repository>()
    private lateinit var viewModel: ItemViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ItemViewModel(repository)
    }

    @Test
    fun `fetchItems should update items LiveData with grouped and sorted data`() = runTest {
        // Mock API response
        val mockItems = listOf(
            Item(1, 1, "Item 1"),
            Item(2, 1, "Item 2"),
            Item(3, 2, "Item 3"),
            Item(4, 2, null),    // Should be filtered out
            Item(5, 2, ""),      // Should be filtered out
            Item(6, 3, "Item 6")
        )

        val expectedResult = mapOf(
            1 to listOf(Item(1, 1, "Item 1"), Item(2, 1, "Item 2")),
            2 to listOf(Item(3, 2, "Item 3")),
            3 to listOf(Item(6, 3, "Item 6"))
        )

        coEvery { repository.getItems() } returns mockItems

        // Observe LiveData
        val observer = mockk<Observer<Map<Int, List<Item>>>>(relaxed = true)
        viewModel.items.observeForever(observer)

        // Act
        viewModel.fetchItems()
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify
        verify { observer.onChanged(expectedResult) }
        assertEquals(expectedResult, viewModel.items.value)

        // Cleanup
        viewModel.items.removeObserver(observer)
    }

    @Test
    fun `fetchItems should set loading LiveData to true and then false`() = runTest {
        coEvery { repository.getItems() } returns emptyList()

        // Observe loading LiveData
        val observer = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.loading.observeForever(observer)

        // Act
        viewModel.fetchItems()
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify loading sequence
        verifySequence {
            observer.onChanged(true)  // Loading starts
            observer.onChanged(false) // Loading ends
        }

        // Cleanup
        viewModel.loading.removeObserver(observer)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

}