package com.id.newsapp

import com.id.newsapp.fake.FakeUserRepository
import com.id.newsapp.screen.register.viewmodel.RegisterViewModel
import com.id.newsapp.screen.register.viewmodel.event.RegisterEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RegisterViewModelTest {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var fakeRepository: FakeUserRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeRepository = FakeUserRepository()
        viewModel = RegisterViewModel(fakeRepository)
    }

    @Test
    fun `register user with new email returns success`(): Unit = runTest {
        viewModel.onEvent(RegisterEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(RegisterEvent.PasswordChanged("123456"))
        viewModel.onEvent(RegisterEvent.Submit)

        val state = viewModel.state.value
        assertTrue(state.isRegistered)
        assertNull(state.error)
    }

    @Test
    fun `register with existing email returns error`(): Unit = runTest {
        fakeRepository.register("existing@example.com", "123")

        viewModel.onEvent(RegisterEvent.EmailChanged("existing@example.com"))
        viewModel.onEvent(RegisterEvent.PasswordChanged("123"))
        viewModel.onEvent(RegisterEvent.Submit)

        val state = viewModel.state.value
        assertFalse(state.isRegistered)
        assertEquals("Email sudah terdaftar.", state.error)
    }
}