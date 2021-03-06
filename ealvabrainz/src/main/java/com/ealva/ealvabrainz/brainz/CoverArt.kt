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

package com.ealva.ealvabrainz.brainz

import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

@Suppress("MaxLineLength")
/**
 * Retrofit interface to get images from Cover Art Archive
 *
 * Possible response status codes are:
 * * 200 if there is a release with this MBID.
 * * 400 if {mbid} cannot be parsed as a valid UUID.
 * * 404 if there is no release with this MBID.
 * * 405 if the request method is not one of GET or HEAD.
 * * 406 if the server is unable to generate a response suitable to the Accept header.
 * * 503 if the user has exceeded their rate limit.
 *
 * Be sure to read [MusicBrainz requirements](https://musicbrainz.org/doc/XML_Web_Service/Rate_Limiting#Provide_meaningful_User-Agent_strings)
 * for querying their servers.
 */
public interface CoverArt {
  /**
   * An example for looking up release artwork by mbid would be:
   * https://coverartarchive.org/release/91975b77-c9f2-46d1-a03b-f1fffbda1d1c
   *
   * @param entity either "release" or "release-group"
   * @param mbid the release or release-group mbid. In the example this would be:
   * 91975b77-c9f2-46d1-a03b-f1fffbda1d1c
   *
   * @return the [CoverArtRelease] associated with the mbid, wrapped in a [Response]
   */
  @GET("{entity}/{mbid}")
  public suspend fun getArtwork(
    @Path("entity") entity: String,
    @Path("mbid") mbid: String
  ): Response<CoverArtRelease>
}
