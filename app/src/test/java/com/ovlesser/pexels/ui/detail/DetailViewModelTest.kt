package com.ovlesser.pexels.ui.detail

import android.app.Application
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.ovlesser.pexels.data.Data
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class DetailViewModelTest: Spek({
    val mockApplication = mockk<Application>() {
        every { applicationContext } returns mockk()
    }
    val photo = Data.Photo(id = 123, width = 200, height = 120)
    lateinit var detailViewModel: DetailViewModel

    beforeGroup {
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
        detailViewModel = spyk(DetailViewModel(photo, mockApplication))
    }

    describe("init") {
        it("should init successfully") {
            Assertions.assertThat(detailViewModel.photo.value).isEqualTo(photo)
        }

        it("should return correct size") {
            detailViewModel.size.observeForever {
                Assertions.assertThat(detailViewModel.size.value).isEqualTo("${photo.width} * ${photo.height}")
            }
            detailViewModel.size.removeObserver {}
        }
    }
})