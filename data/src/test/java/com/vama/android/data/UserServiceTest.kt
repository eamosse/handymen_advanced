package com.vama.android.data

import com.vama.android.data.api.InMemoryUserService
import com.vama.android.data.api.SortCriteria
import com.vama.android.data.api.UserService
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepositoryImpl
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class UserServiceTest {
    private lateinit var userService: UserService

    private val testUser = User(
        id = 100L,
        name = "Test User",
        avatarUrl = "https://test.com/avatar.jpg",
        address = "Test City ; 3km",
        phoneNumber = "+33 6 12 34 56 78",
        aboutMe = "Test description",
        favorite = false,
        webSite = "www.test.com"
    )

    @Before
    fun setup() {
        userService = InMemoryUserService()
    }

    // CRUD Operations Tests
    @Test
    fun `test get all users`() {
        val users = userService.getAll()
        assertFalse(users.isEmpty())
    }

    @Test
    fun `test get user by id`() {
        val users = userService.getAll()
        val firstUser = users.first()
        val foundUser = userService.getById(firstUser.id)
        assertNotNull(foundUser)
        assertEquals(firstUser.id, foundUser?.id)
    }

    @Test
    fun `test add new user`() {
        val initialCount = userService.getAll().size
        val addedUser = userService.add(testUser)
        
        assertNotEquals(testUser.id, addedUser.id) // Should have a new ID
        assertEquals(initialCount + 1, userService.getAll().size)
        assertEquals(testUser.name, addedUser.name)
    }

    @Test
    fun `test update user`() {
        val addedUser = userService.add(testUser)
        val updatedUser = addedUser.copy(name = "Updated Name")
        
        val result = userService.update(updatedUser)
        assertEquals("Updated Name", result.name)
        
        val fetchedUser = userService.getById(result.id)
        assertEquals("Updated Name", fetchedUser?.name)
    }

    @Test
    fun `test delete user`() {
        val addedUser = userService.add(testUser)
        val initialCount = userService.getAll().size
        
        userService.delete(addedUser.id)
        assertEquals(initialCount - 1, userService.getAll().size)
        assertNull(userService.getById(addedUser.id))
    }

    // Favorites Management Tests
    @Test
    fun `test toggle favorite`() {
        val addedUser = userService.add(testUser)
        assertFalse(addedUser.favorite)
        
        userService.toggleFavorite(addedUser.id)
        val updatedUser = userService.getById(addedUser.id)
        assertTrue(updatedUser?.favorite ?: false)
        
        userService.toggleFavorite(addedUser.id)
        val toggledBackUser = userService.getById(addedUser.id)
        assertFalse(toggledBackUser?.favorite ?: true)
    }

    @Test
    fun `test get favorites`() {
        val addedUser = userService.add(testUser)
        val initialFavoritesCount = userService.getFavorites().size
        
        userService.toggleFavorite(addedUser.id)
        assertEquals(initialFavoritesCount + 1, userService.getFavorites().size)
    }

    // Search Tests
    @Test
    fun `test search by name`() {
        val addedUser = userService.add(testUser)
        val results = userService.search("Test User")
        assertTrue(results.any { it.id == addedUser.id })
    }

    @Test
    fun `test search by phone`() {
        val addedUser = userService.add(testUser)
        val results = userService.search("12 34")
        assertTrue(results.any { it.id == addedUser.id })
    }

    @Test
    fun `test search by address`() {
        val addedUser = userService.add(testUser)
        val results = userService.search("Test City")
        assertTrue(results.any { it.id == addedUser.id })
    }

    // Sorting Tests
    @Test
    fun `test sort by name ascending`() {
        val sortedUsers = userService.sortBy(SortCriteria.NAME_ASC)
        for (i in 0 until sortedUsers.size - 1) {
            assertTrue(sortedUsers[i].name <= sortedUsers[i + 1].name)
        }
    }

    @Test
    fun `test sort by name descending`() {
        val sortedUsers = userService.sortBy(SortCriteria.NAME_DESC)
        for (i in 0 until sortedUsers.size - 1) {
            assertTrue(sortedUsers[i].name >= sortedUsers[i + 1].name)
        }
    }

    @Test
    fun `test sort by favorite`() {
        val sortedUsers = userService.sortBy(SortCriteria.FAVORITE)
        for (i in 0 until sortedUsers.size - 1) {
            if (sortedUsers[i].favorite && !sortedUsers[i + 1].favorite) {
                continue
            }
            if (!sortedUsers[i].favorite && sortedUsers[i + 1].favorite) {
                fail("Favorites should be sorted first")
            }
        }
    }

    @Test
    fun `test sort by distance`() {
        val sortedUsers = userService.sortBy(SortCriteria.DISTANCE)
        for (i in 0 until sortedUsers.size - 1) {
            val distance1 = extractDistance(sortedUsers[i].address)
            val distance2 = extractDistance(sortedUsers[i + 1].address)
            assertTrue(distance1 <= distance2)
        }
    }

    private fun extractDistance(address: String): Int {
        return address.split(";").getOrNull(1)?.trim()?.replace("km", "")?.toIntOrNull() ?: 0
    }
}

/**
 * Tests for UserRepository to ensure proper delegation to UserService
 */
class UserRepositoryTest {
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setup() {
        repository = UserRepositoryImpl.instance() as UserRepositoryImpl
    }

    @Test
    fun `test repository singleton pattern`() {
        val repo1 = UserRepositoryImpl.instance()
        val repo2 = UserRepositoryImpl.instance()
        assertSame(repo1, repo2)
    }

    @Test
    fun `test repository delegates to service`() {
        val users = repository.getAll()
        assertNotNull(users)
        // More specific delegation tests could be added here if needed
    }
}