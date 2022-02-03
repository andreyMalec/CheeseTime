package com.malec.cheesetime.repo

import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.model.filteredBy
import com.malec.domain.api.CheeseApi
import com.malec.domain.api.StorageApi
import com.malec.domain.model.Cheese
import com.malec.domain.repository.CheeseRepo
import com.malec.domain.util.CheeseSharer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CheeseRepoTest {
    @MockK
    lateinit var api: CheeseApi

    @MockK
    lateinit var storageApi: StorageApi

    @MockK
    lateinit var sharer: CheeseSharer

    private lateinit var repo: CheeseRepo

    companion object {
        private var nextId = 1L
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        repo = CheeseRepo(api, storageApi, sharer)
        initApiMock()
    }

    @Test
    fun testGetAll() = runBlocking {
        val data = repo.getAll()
        assertEquals(testData[0], data[0])
        assertEquals(3, data.size)
    }

    @Test
    fun testGetAllSelected() = runBlocking {
        repo.toggleSelect(testData[0])
        repo.toggleSelect(testData[1])
        val data = repo.getAllSelected()
        assertEquals(testData[1], data[0])
        assertEquals(1, data.size)
    }

    @Test
    fun testGetNextId() = runBlocking {
        val firstId = repo.getNextId()
        assertEquals(1, firstId)
        val secondId = repo.getNextId()
        assertEquals(2, secondId)
    }

    @Test
    fun testGetAllFiltered() = runBlocking {
        val data = repo.getAllFilteredFlow(parmesanFilter).first()
        assertEquals(0, data.size)

        repo.create(newParmesan)
        val updatedData = repo.getAllFilteredFlow(parmesanFilter).first()
        assertEquals(1, updatedData.size)
        assertEquals(newParmesan, updatedData[0])
    }

    @Test
    fun testGetAllTitleContains() = runBlocking {
        val data = repo.getAllTitleContainsFlow(parmesanFilter, "new parmesan").first()
        assertEquals(0, data.size)

        repo.create(newParmesan)
        val updatedData = repo.getAllTitleContainsFlow(parmesanFilter, "new parmesan").first()
        assertEquals(0, updatedData.size)

        repo.create(anotherNewParmesan)
        val updatedData2 = repo.getAllTitleContainsFlow(parmesanFilter, "new parmesan").first()
        assertEquals(1, updatedData2.size)

        assertEquals(anotherNewParmesan, updatedData2[0])

        val newData = repo.getAllTitleContainsFlow(emptyFilter, "name").first()
        assertEquals(3, newData.size)
    }

    @Test
    fun testCreateAndGetById() = runBlocking {
        val data = repo.getById(50)
        assertNull(data)
        repo.create(newParmesan)
        val newData = repo.getById(50)
        assertNotNull(newData)
    }

    @Test
    fun testCreate() = runBlocking {
        val data = repo.getById(50)
        assertNull(data)
        repo.create(newParmesan)
        val newData = repo.getById(50)
        assertNotNull(newData)
        assertEquals(4, testData.size)
    }

    @Test
    fun testUpdate() = runBlocking {
        assertFalse(testData[1].isArchived)
        repo.update(testData[1])
        assertTrue(testData[1].isArchived)

        assertFalse(newParmesan.isArchived)
        repo.update(newParmesan)
        assertFalse(newParmesan.isArchived)
    }

    @Test
    fun testDeleteById() = runBlocking {
        assertNull(repo.getById(50))
        repo.create(newParmesan)
        assertEquals(4, testData.size)
        assertNotNull(repo.getById(50))
        repo.delete(50)
        assertNull(repo.getById(50))
        assertEquals(3, testData.size)
    }

    @Test
    fun testSelect() = runBlocking {
        repo.toggleSelect(newParmesan)
        repo.toggleSelect(testData[0])
        assertEquals(1, repo.getSelectedIds().size)

        repo.update(testData[0].also { it.isSelected = false })
        repo.toggleSelect(testData[0])
        assertEquals(2, repo.getSelectedIds().size)

        repo.archiveSelected()
        assertFalse(newParmesan.isArchived)
        assertTrue(testData[0].isArchived)

        repo.toggleSelect(testData[0])
        repo.deleteSelected()
        assertNull(repo.getById(0))
    }

    private fun initApiMock() {
        coEvery {
            api.getNextId()
        } answers {
            nextId++
        }
        coEvery {
            api.getAll()
        } answers {
            testData
        }
        coEvery {
            api.getAllFilteredFlow(any())
        } answers {
            flowOf(testData.filteredBy(firstArg()))
        }
        coEvery {
            api.getById(any())
        } answers {
            testData.find { it.id == firstArg() }
        }
        coEvery {
            api.create(any())
        } answers {
            testData.add(firstArg())
        }
        coEvery {
            api.update(any())
        } answers {
            testData.map {
                if (it.id == (firstArg() as Cheese).id)
                    firstArg()
                else
                    it
            }
        }
        coEvery {
            api.deleteById(any())
        } answers {
            testData.removeIf { it.id == firstArg() }
        }
    }

    private val parmesanFilter = CheeseFilter(type = "parmesan")

    private val emptyFilter = CheeseFilter()

    private val testData = mutableListOf(
        Cheese(
            0,
            "name",
            0,
            0,
            "recipe",
            "comment",
            "milkType",
            "milkVolume",
            "milkAge",
            "composition",
            listOf("stage1", "stage2"),
            "volume",
            "volumeMax",
            0,
            isSelected = true,
            isArchived = false,
            listOf("photo1", "photo2")
        ),
        Cheese(
            2,
            "name2",
            20,
            20,
            "recipe2",
            "comment2",
            "milkType2",
            "milkVolume2",
            "milkAge2",
            "composition2",
            listOf("stage12", "stage22"),
            "0",
            "volumeMax2",
            20,
            isSelected = false,
            isArchived = false,
            listOf("photo12", "photo22")
        ),
        Cheese(
            5,
            "test",
            520,
            520,
            "parmesan",
            "",
            "cow",
            "0.5",
            "1",
            "",
            listOf("", ""),
            "123",
            "456",
            20,
            isSelected = false,
            isArchived = true,
            listOf("photo512", "photo522")
        )
    )

    private val newParmesan = Cheese(
        50,
        "name",
        5203,
        5203,
        "parmesan",
        "",
        "cow",
        "0.9",
        "1",
        "",
        listOf("", ""),
        "456",
        "456",
        20,
        isSelected = false,
        isArchived = false,
        listOf("photo51", "photo52")
    )

    private val anotherNewParmesan = Cheese(
        90,
        "another new parmesan",
        503,
        203,
        "parmesan",
        "",
        "cow",
        "1.2",
        "1",
        "",
        listOf("", ""),
        "456",
        "456",
        20,
        isSelected = false,
        isArchived = false,
        listOf("photo151", "photo152")
    )
}