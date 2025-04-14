package com.id.newsapp

import com.id.domain.model.NewsCategory
import com.id.domain.usecase.GetArticlesUseCase
import com.id.domain.usecase.GetBlogsUseCase
import com.id.domain.usecase.GetNewsDetailUseCase
import com.id.domain.usecase.GetReportsUseCase
import com.id.newsapp.fake.FakeNewsRepository
import com.id.newsapp.fake.FakeUserRepository
import com.id.newsapp.screen.homescreen.viewmodel.HomeViewModel
import com.id.newsapp.screen.homescreen.viewmodel.event.HomeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: HomeViewModel
    private val fakeNewsRepository = FakeNewsRepository()
    private val fakeUserRepository = FakeUserRepository()

    private val getArticlesUseCase = GetArticlesUseCase(fakeNewsRepository)
    private val getBlogsUseCase = GetBlogsUseCase(fakeNewsRepository)
    private val getReportsUseCase = GetReportsUseCase(fakeNewsRepository)
    private val getNewsDetailUseCase = GetNewsDetailUseCase(fakeNewsRepository)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = HomeViewModel(
            getArticlesUseCase = getArticlesUseCase,
            getBlogsUseCase = getBlogsUseCase,
            getReportsUseCase = getReportsUseCase,
            getNewsDetailUseCase = getNewsDetailUseCase,
            repository = fakeUserRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ToggleSortOrder should reverse sort order`() = runTest {
        viewModel.onEvent(HomeEvent.LoadItemByCategory(NewsCategory.ARTICLES))
        advanceUntilIdle()

        val original = viewModel.state.value.articles
        viewModel.onEvent(HomeEvent.ToggleSortOrder)
        advanceUntilIdle()

        val updated = viewModel.state.value.articles
        assertNotEquals(original.map { it.title }, updated.map { it.title })
    }

    @Test
    fun `Logout updates isLoggedOut state to true`() = runTest {
        viewModel.onEvent(HomeEvent.Logout)
        advanceUntilIdle()

        assert(viewModel.state.value.isLoggedOut)
    }

    @Test
    fun `LoadEmail updates state with email`() = runTest {
        fakeUserRepository.setEmail("fajar@mail.com")
        viewModel.onEvent(HomeEvent.LoadEmail)
        advanceUntilIdle()

        assert(viewModel.state.value.email == "fajar@mail.com")
    }

    @Test
    fun `AddToRecentSearches adds new query`() = runTest {
        viewModel.onEvent(HomeEvent.AddToRecentSearches("SpaceX"))
        val recent = viewModel.state.value.recentSearches

        assert(recent.contains("SpaceX"))
    }
}