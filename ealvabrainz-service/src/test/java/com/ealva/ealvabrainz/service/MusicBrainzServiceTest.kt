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

package com.ealva.ealvabrainz.service

import com.ealva.ealvabrainz.MainCoroutineRule
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
import com.ealva.ealvabrainz.brainz.data.RecordingList
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.Release.Companion.NullRelease
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseList
import com.ealva.ealvabrainz.brainz.data.toReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.toReleaseMbid
import com.ealva.ealvabrainz.common.AlbumName
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.toAlbumName
import com.ealva.ealvabrainz.common.toArtistName
import com.ealva.ealvabrainz.common.toRecordingName
import com.ealva.ealvabrainz.runBlockingTest
import com.ealva.ealvabrainz.service.MusicBrainzResult.Exceptional
import com.ealva.ealvabrainz.service.MusicBrainzResult.Success
import com.nhaarman.expect.expect
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class MusicBrainzServiceTest {
  @get:Rule var coroutineRule = MainCoroutineRule()

  private val dummyException = RuntimeException("I O, let's go!")

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test findRelease`() = coroutineRule.runBlockingTest {
    doTestFindRelease(null, null)
    doTestFindRelease(20, null)
    doTestFindRelease(null, 10)
    doTestFindRelease(100, 10)
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test findRelease on thrown exception`() = coroutineRule.runBlockingTest {
    val artistName = ArtistName("David Bowie")
    val albumName = AlbumName("The Man Who Sold the World")
    val query = """artist:"${artistName.value}" AND release:"${albumName.value}""""
    val mockBrainz = mock<MusicBrainz> {
      onBlocking {
        findRelease(query)
      } doThrow MusicBrainzException("msg", dummyException)
    }
    val service = makeServiceForTest(mockBrainz)
    expect(service.findRelease(artistName, albumName)).toBeInstanceOf<Exceptional> { result ->
      expect(result.exception).toBeInstanceOf<MusicBrainzException> { ex ->
        expect(ex.cause).toBeTheSameAs(dummyException)
      }
    }
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test lookupRelease`() = coroutineRule.runBlockingTest {
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseMbid()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value, null) } doReturn makeSuccess(NullRelease)
    }
    val service = makeServiceForTest(mockBrainz)
    val release = service.lookupRelease(mbid)
    expect(release).toNotBeNull()
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test lookupRelease with includes`() = coroutineRule.runBlockingTest {
    val dummy = Release("dummy")
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseMbid()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value, null, null, null) } doReturn makeSuccess(dummy)
    }
    val service = makeServiceForTest(mockBrainz)
    expect(
      service.lookupRelease(mbid)
    ).toBeInstanceOf<Success<Release>> { result ->
      expect(result.value).toBeTheSameAs(dummy)
    }
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test lookupReleaseGroup status-include mismatch`() = coroutineRule.runBlockingTest {
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseGroupMbid()
    val mockBrainz = mock<MusicBrainz>()  // should not be called
    val service = makeServiceForTest(mockBrainz)
    // expect lookupReleaseGroup to throw
    expect(
      service.lookupReleaseGroup(
        mbid,
        status = Release.Status.Official
      )
    ).toBeInstanceOf<Exceptional> { result ->
      expect(result.exception).toBeInstanceOf<MusicBrainzException> {

      }
    }
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test lookupReleaseGroup status used`() = coroutineRule.runBlockingTest {
    val dummy = ReleaseGroup(title = "dummy")
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseGroupMbid()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking {
        lookupReleaseGroup(mbid.value, include = "releases", status = "official")
      } doReturn makeSuccess(dummy)
    }
    val service = makeServiceForTest(mockBrainz)
    expect(
      service.lookupReleaseGroup(
        mbid,
        listOf(ReleaseGroup.Subquery.Releases),
        status = Release.Status.Official
      )
    ).toBeInstanceOf<Success<ReleaseGroup>> { result ->
      expect(result.value).toBeTheSameAs(dummy)
    }
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test find recording query string`() = coroutineRule.runBlockingTest {
    val dummy = RecordingList()
    val recordingName = "Her Majesty".toRecordingName()
    val albumName = "Abbey Road".toAlbumName()
    val artistName = "The Beatles".toArtistName()
    val query =
      """recording:"${recordingName.value}" AND artist:"${artistName.value}" AND release:"${albumName.value}""""
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { findRecording(query, null, null) } doReturn makeSuccess(dummy)
    }
    val service = makeServiceForTest(mockBrainz)
    expect(
      service.findRecording(
        recordingName,
        artistName,
        albumName
      )
    ).toBeInstanceOf<Success<RecordingList>> { result ->
      expect(result.value).toBeTheSameAs(dummy)
    }
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test direct brainz call`() = coroutineRule.runBlockingTest {
    val dummy = Release(title = "dummy")
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseMbid()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value, null) } doReturn makeSuccess(dummy)
    }
    val service = makeServiceForTest(mockBrainz)
    val response = service.brainz { brainz ->
      brainz.lookupRelease(mbid.value, null)
    }
    expect(response).toBeInstanceOf<Success<Release>> {
      expect(it.value).toBe(dummy)
    }
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test direct brainz call error response`() = coroutineRule.runBlockingTest {
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseMbid()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking {
        lookupRelease(mbid.value, null)
      } doReturn Response.error(404, notFoundBody.toResponseBody())
    }
    val service = makeServiceForTest(mockBrainz)
    expect(service.brainz { brainz ->
      brainz.lookupRelease(mbid.value, null)
    }).toBeInstanceOf<MusicBrainzResult.Error> { result ->
      expect(result.error.error).toBe("404")
      expect(result.error.help).toBe("Not found")
    }
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test direct brainz call exception`() = coroutineRule.runBlockingTest {
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseMbid()
    val dummyEx = IllegalStateException()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value, null) } doThrow dummyEx
    }
    val service = makeServiceForTest(mockBrainz)
    expect(service.brainz { brainz ->
      brainz.lookupRelease(mbid.value, null)
    }).toBeInstanceOf<Exceptional> { result ->
      expect(result.exception).toBeInstanceOf<MusicBrainzException> { ex ->
        expect(ex.cause).toBeTheSameAs(dummyEx)
      }
    }
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test direct brainz call unknown error response`() = coroutineRule.runBlockingTest {
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseMbid()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking {
        lookupRelease(mbid.value, null)
      } doReturn Response.error(404, "won't work".toResponseBody())
    }
    val service = makeServiceForTest(mockBrainz)
    expect(service.brainz { brainz ->
      brainz.lookupRelease(mbid.value, null)
    }).toBeInstanceOf<Exceptional> { result ->
      expect(result.exception).toBeInstanceOf<MusicBrainzUnknownError> { ex ->
        expect(ex.rawResponse.httpStatusCode).toBe(404)
      }
    }
  }

  private suspend fun doTestFindRelease(limit: Int?, offset: Int?) {
    val dummy = ReleaseList()
    val artistName = ArtistName("David Bowie")
    val albumName = AlbumName("The Man Who Sold the World")
    val query = """artist:"${artistName.value}" AND release:"${albumName.value}""""
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { findRelease(query, limit, offset) } doReturn makeSuccess(dummy)
    }
    val service = makeServiceForTest(mockBrainz)
    expect(
      service.findRelease(
        artistName,
        albumName,
        limit,
        offset
      )
    ).toBeInstanceOf<Success<ReleaseList>> { result ->

      verify(mockBrainz, times(1)).findRelease(query, limit, offset)
      expect(result.value).toBe(dummy)
    }
  }

  private fun makeServiceForTest(mockBrainz: MusicBrainz): MusicBrainzService {
    return MusicBrainzService.make(
      mockBrainz,
      NullCoverArtService,
      coroutineRule.testDispatcher
    )
  }

  private fun <T> makeSuccess(t: T) = Response.success(200, t)

}

object NullCoverArtService : CoverArtService {
  override suspend fun getCoverArtRelease(
    entity: CoverArtService.Entity,
    mbid: String
  ): CoverArtRelease? {
    return null
  }
}

private const val notFoundBody = """
{
  "error": "404",
  "help": "Not found"
}
"""
