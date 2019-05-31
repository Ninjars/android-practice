package net.jeremystevens.apipractice.features.swapi.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SWAPIService {
    @GET("people/")
    fun getPeople(@Query("page") page: Int): Deferred<PeopleResult>

    @GET("people/{id}/")
    fun getPerson(@Path("id") id: Int): Deferred<PersonResult>

    @GET("planets/{id}/")
    fun getPlanet(@Path("id") id: Int): Deferred<PlanetResult>
}

data class PeopleResult(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<PersonResult>
)

data class PersonResult(
    val name: String,
    val url: String,
    val homeworld: String,
    val films: List<String>,
    val vehicles: List<String>,
    val starships: List<String>
)

data class PlanetResult(
        val name: String,
        val climate: String,
        val terrain: String,
        val population: String,
        val diameter: String,
        val rotation_period: String,
        val orbital_period: String
)

//"name": "Luke Skywalker",
//"height": "172",
//"mass": "77",
//"hair_color": "blond",
//"skin_color": "fair",
//"eye_color": "blue",
//"birth_year": "19BBY",
//"gender": "male",
//"homeworld": "https://swapi.co/api/planets/1/",
//"films": [
//"https://swapi.co/api/films/2/",
//"https://swapi.co/api/films/6/",
//"https://swapi.co/api/films/3/",
//"https://swapi.co/api/films/1/",
//"https://swapi.co/api/films/7/"
//],
//"species": [
//"https://swapi.co/api/species/1/"
//],
//"vehicles": [
//"https://swapi.co/api/vehicles/14/",
//"https://swapi.co/api/vehicles/30/"
//],
//"starships": [
//"https://swapi.co/api/starships/12/",
//"https://swapi.co/api/starships/22/"
//],
//"created": "2014-12-09T13:50:51.644000Z",
//"edited": "2014-12-20T21:17:56.891000Z",
//"url": "https://swapi.co/api/people/1/"