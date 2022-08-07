package jp.co.yumemi.android.code_check

import android.os.Parcelable
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

class GitHubSearchRepository {
    private val client = HttpClient(Android)

    suspend fun searchRepository(inputText: String): List<Item> {
        val jsonString = try {
            withContext(Dispatchers.IO) {
                client.get<String>("https://api.github.com/search/repositories") {
                    header("Accept", "application/vnd.github.v3+json")
                    parameter("q", inputText)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
        val result = runCatching {
            JSONObject(jsonString)
        }.map {
            it.optJSONArray("items")!!
        }.map { array ->
            val items = mutableListOf<Item>()
            for (i in 0 until array.length()) {
                val item = array.optJSONObject(i)!!
                items.add(
                    Item(
                        name = item.optString("full_name"),
                        ownerIconUrl = item.optJSONObject("owner")!!.optString("avatar_url"),
                        language = item.optString("language"),
                        stargazersCount = item.optLong("stargazers_count"),
                        watchersCount = item.optLong("watchers_count"),
                        forksCount = item.optLong("forks_count"),
                        openIssuesCount = item.optLong("open_issues_count")
                    )
                )
            }
            items
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault(emptyList())
        return result
    }
}

@Parcelize
data class Item(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable
