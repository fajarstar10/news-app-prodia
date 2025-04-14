package com.id.newsapp

import com.id.newsapp.fake.FakeUserRepository
import com.id.newsapp.screen.login.viewmodel.LoginViewModel
import com.id.newsapp.screen.login.viewmodel.event.LoginEvent
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeRepository: FakeUserRepository
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        fakeRepository = FakeUserRepository().apply {
            runTest { register("user@mail.com", "123456") }
        }

        viewModel = LoginViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login berhasil dengan kredensial benar`() = runTest {
        viewModel.onEvent(LoginEvent.EmailChanged("user@mail.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("123456"))
        viewModel.onEvent(LoginEvent.Submit)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.isLoggedIn)
        assertNull(state.error)
    }

    @Test
    fun `login gagal dengan password salah`() = runTest {
        viewModel.onEvent(LoginEvent.EmailChanged("user@mail.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("wrongpass"))
        viewModel.onEvent(LoginEvent.Submit)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Password salah", state.error)
        assertTrue(!state.isLoggedIn)
    }
}