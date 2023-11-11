package com.cube.cubeacademy

import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.DataWrapper
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import javax.inject.Inject

@HiltAndroidTest
class RepositoryTest {
	@get:Rule
	var hiltRule = HiltAndroidRule(this)

	@Inject
	lateinit var repository: Repository

	@Before
	fun setUp() {
		hiltRule.inject()
	}

	@Test
	fun getNominationsTest() = runBlocking{
		// mocked response
		val response: Response<DataWrapper<List<Nomination>>> = repository.getAllNominations()

		// assert that the call is successful
		assertEquals(true, response.isSuccessful)

		// assert the size of nominations
		assertEquals(4, response.body()?.data?.size)

	}

	@Test
	fun getNomineesTest() = runBlocking{
		// mocked response
		val response: Response<DataWrapper<List<Nominee>>> = repository.getAllNominees()

		// assert that the call is successful
		assertEquals(true, response.isSuccessful)

		// assert the size of nominations
		assertEquals(3, response.body()?.data?.size)
	}

	@Test
	fun createNominationTest() = runBlocking{
		// mocked nomination
		val nomination = Nomination("3","efd0-42d4", "awesome workmate!","fair", "2023-10-11", "2023-11-12")

		// mocked response
		val response: Response<DataWrapper<Nomination>>? = repository.createNomination(nomination.nomineeId, nomination.reason, nomination.process)

		// assert that the call is successful
		assertEquals(true, response?.isSuccessful)

		// assert that the nomination was created
		assertEquals(nomination, response?.body()?.data)
	}
}