package com.ovlesser.pexels.ui.home

import android.app.Application
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.ovlesser.pexels.MockApiService
import com.ovlesser.pexels.MockDao
import com.ovlesser.pexels.data.Data
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class HomeViewModelTest: Spek({
    val mockApplication = mockk<Application>() {
        every { applicationContext } returns mockk()
    }
    val data = Data( photos = listOf(Data.Photo(id = 111), Data.Photo(id = 222), Data.Photo(id = 333)), totalResults = 3)
    val mockApiService = MockApiService(data)
    val mockDao = MockDao(data)
    lateinit var homeViewModel: HomeViewModel
    val mainThreadSurrogate = newSingleThreadContext("UI thread")

    beforeGroup {
        Dispatchers.setMain(mainThreadSurrogate)
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
        homeViewModel = spyk(HomeViewModel(apiService = mockApiService, dao = mockDao, application = mockApplication))
    }

    afterGroup {
        ArchTaskExecutor.getInstance().setDelegate(null)
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    describe("init") {
        it("should init successfully") {
            homeViewModel.data.observeForever {
                Assertions.assertThat(homeViewModel.data.value).isEqualTo(data)
            }
        }
    }

    describe("#${HomeViewModel::refreshRepository.name}") {
        it("should return") {
            homeViewModel.refreshRepository("")
            Assertions.assertThat(homeViewModel.status.value).isEqualTo(PexelsApiStatus.DONE)
        }
    }

    describe("#${HomeViewModel::displayPhotoDetails.name}") {
        it("should return") {
            val photo = Data.Photo(id = 123)
            homeViewModel.displayPhotoDetails(photo)
            Assertions.assertThat(homeViewModel.selectedPhoto.value).isEqualTo(photo)
        }
    }

    describe("#${HomeViewModel::displayPhotoDetailComplete.name}") {
        it("should return null") {
            homeViewModel.displayPhotoDetailComplete()
            Assertions.assertThat(homeViewModel.selectedPhoto.value).isNull()
        }
    }
})