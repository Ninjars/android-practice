package net.jeremystevens.apipractice.features.coroutine.domain

import net.jeremystevens.apipractice.features.coroutine.network.SWAPIService
import net.jeremystevens.apipractice.features.coroutine.network.PersonResult
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class PersonRepository @Inject constructor(private val service: SWAPIService) {

    private var currentPeopleData = PeopleData(Int.MAX_VALUE, 0, emptyMap())

    @Throws(ArrayIndexOutOfBoundsException::class, NoSuchElementException::class, HttpException::class)
    suspend fun getPerson(index: Int): PersonData {
        if (index > currentPeopleData.maxPeople) throw ArrayIndexOutOfBoundsException("there are only ${currentPeopleData.maxPeople} results available")

        var fetched = true
        while (index >= currentPeopleData.people.count() && fetched) {
            fetched = try {
                fetchNextPeopleBatch()
            } catch (e: HttpException) {
                false
            }
        }
        if (index >= currentPeopleData.people.count()) {
            throw NoSuchElementException("unable to find item at index $index")
        }
        return currentPeopleData.people.toSortedMap().values.toList()[index]
    }

    @Throws(HttpException::class)
    suspend fun getPersonWithId(id: Int): PersonData {
        if (id > currentPeopleData.maxPeople) throw ArrayIndexOutOfBoundsException("there are only ${currentPeopleData.maxPeople} results available")

        if (!currentPeopleData.people.contains(id)) {
            val personResult = service.getPerson(id).await()
            val data = resultToData(personResult)
            currentPeopleData = PeopleData(
                currentPeopleData.maxPeople,
                currentPeopleData.currentPage,
                currentPeopleData.people + (data.id to data)
            )
        }
        return currentPeopleData.people.getValue(id)
    }

    private suspend fun fetchNextPeopleBatch(): Boolean {
        val nextPage = currentPeopleData.currentPage + 1
        val newData = service.getPeople(nextPage).await()
        Timber.i("fetchNextPeopleBatch: $newData")
        if (newData.results.isEmpty()) return false

        val newPeople = newData.results
            .map(::resultToData)
            .map { it.id to it }
            .toMap()

        currentPeopleData = PeopleData(newData.count, nextPage, currentPeopleData.people + newPeople)
        return true
    }

    private fun resultToData(result: PersonResult): PersonData {
        return PersonData(
            getIdFromUrl(result.url),
            result.name,
            getIdFromUrl(result.homeworld),
            result.films.map(::getIdFromUrl),
            result.vehicles.map(::getIdFromUrl),
            result.starships.map(::getIdFromUrl)
        )
    }

    /**
     * Assumes the supplied string ends with ".../<number>/"
     */
    private fun getIdFromUrl(url: String): Int {
        val index = url.dropLast(1).lastIndexOf("/")
        return url.substring(startIndex = index + 1, endIndex = url.length - 1).toInt()
    }
}

data class PeopleData(
    val maxPeople: Int,
    val currentPage: Int,
    val people: Map<Int, PersonData>
)

data class PersonData(
    val id: Int,
    val name: String,
    val homeworldId: Int,
    val filmIds: List<Int>,
    val vehicleIds: List<Int>,
    val starshipIds: List<Int>
)