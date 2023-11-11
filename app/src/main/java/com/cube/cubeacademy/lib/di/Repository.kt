package com.cube.cubeacademy.lib.di

import com.cube.cubeacademy.lib.api.ApiService
import com.cube.cubeacademy.lib.models.DataWrapper
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import retrofit2.Response

class Repository(val api: ApiService) {

	suspend fun getAllNominations(): Response<DataWrapper<List<Nomination>>> {
		return  try {
			api.getAllNominations()
	} catch (e: Exception) {
		// handle exceptions
		Response.error(500, okhttp3.ResponseBody.create(null, "An error occurred: ${e.message}"))
	}
	}

	suspend fun getAllNominees(): Response<DataWrapper<List<Nominee>>> {
		return  try {
			api.getAllNominees()
		} catch (e: Exception) {
			// handle exceptions
			Response.error(500, okhttp3.ResponseBody.create(null, "An error occurred: ${e.message}"))
		}
	}

	suspend fun createNomination(nomineeId: String, reason: String, process: String):
			Response<DataWrapper<Nomination>>? {
		return try {
			api.createNomination(nomineeId, reason, process)
		} catch (e: Exception) {
			// handle exceptions
			Response.error(500, okhttp3.ResponseBody.create(null, "An error occurred: ${e.message}"))
		}
	}
}