/*
 * Copyright (c) 2020  Eric A. Snell
 *
 * This file is part of eAlvaBrainz
 *
 * eAlvaBrainz is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 * eAlvaBrainz is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaBrainz.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.ealva.ealvabrainz.brainz.data

import com.ealva.ealvabrainz.brainz.data.Url.Companion.NullUrl
import com.squareup.moshi.JsonClass
import timber.log.Timber

/**
 * A MusicBrainz URL consists of its ID and the actual Url
 *
 * Example
 * ```json
 * "url": {
 *     "id": "56da3f0f-2a88-44ab-97ad-9cf5fa1d0be6",
 *     "resource": "https://www.musik-sammler.de/album/6995/"
 *  }
 * ```
 */
@JsonClass(generateAdapter = true)
class Url(
  /** UUID of the Url instance */
  var id: String = "",
  /** The url */
  var resource: String = ""
) {

  interface Lookup : Include

  /**
   * * [Url-Work](https://musicbrainz.org/relationships/url-work)
   */
  enum class Relation(override val value: String) : Lookup {
    Work("work-rels")
  }

  companion object {
    val NullUrl = Url(id = NullObject.ID)
    val fallbackMapping: Pair<String, Any> = Url::class.java.name to NullUrl
  }
}

inline val Url.isNullObject: Boolean
  get() = this === NullUrl

inline class UrlMbid(override val value: String) : Mbid

inline val Url.mbid: UrlMbid
  get() = id.toUrlMbid()

@Suppress("NOTHING_TO_INLINE")
inline fun String.toUrlMbid(): UrlMbid {
  if (Mbid.logInvalidMbid && isInvalidMbid()) Timber.w("Invalid UrlMbid")
  return UrlMbid(this)
}
