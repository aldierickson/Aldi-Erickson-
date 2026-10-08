package com.example.data.gemini

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class GeminiGenerateRequest(
    val contents: List<GeminiContent>,
    val tools: List<GeminiTool>? = null,
    val systemInstruction: GeminiContent? = null
)

@Serializable
data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String? = null
)

@Serializable
data class GeminiPart(
    val text: String? = null
)

@Serializable
data class GeminiTool(
    val googleSearch: JsonObject = JsonObject(emptyMap())
)

@Serializable
data class GeminiGenerateResponse(
    val candidates: List<GeminiCandidate>? = null
)

@Serializable
data class GeminiCandidate(
    val content: GeminiContent? = null,
    val groundingMetadata: GeminiGroundingMetadata? = null,
    val finishReason: String? = null
)

@Serializable
data class GeminiGroundingMetadata(
    val webSearchQueries: List<String>? = null,
    val groundingChunks: List<GeminiGroundingChunk>? = null,
    val searchEntryPoint: JsonObject? = null
)

@Serializable
data class GeminiGroundingChunk(
    val web: GeminiWebSource? = null
)

@Serializable
data class GeminiWebSource(
    val uri: String? = null,
    val title: String? = null
)

data class MarketUpdateState(
    val isLoading: Boolean = false,
    val summary: String = "",
    val newsSnippets: List<MarketNewsSnippet> = emptyList(),
    val sources: List<MarketSource> = emptyList(),
    val searchQueries: List<String> = emptyList(),
    val isGrounded: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    val errorMessage: String? = null
)

data class MarketNewsSnippet(
    val title: String,
    val summary: String,
    val category: String,
    val sourceName: String? = null,
    val sourceUrl: String? = null
)

data class MarketSource(
    val title: String,
    val url: String
)
