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

package com.ealva.brainzsvc.service

import com.ealva.ealvabrainz.test.shared.MainCoroutineRule
import org.junit.Rule

internal class CoverArtServiceTest {
  @get:Rule
  var coroutineRule = MainCoroutineRule()

//  @Test
//  @OptIn(ExperimentalCoroutinesApi::class)
//  fun `get artwork success with Null Object`() = coroutineRule.runBlockingTest {
//    val entity = ReleaseEntity
//    val mbid = "mbid"
//    val mock = mock<CoverArt> {
//      onBlocking {
//        getArtwork(entity.value, mbid)
//      } doReturn Response.success(200, NullCoverArtRelease)
//    }
//    val service = CoverArtService.make(mock, coroutineRule.testDispatcher)
//    val release = service.getCoverArtRelease(entity, mbid)
//    verify(mock).getArtwork(entity.value, mbid)
//    expect(release).toBeTheSameAs(NullCoverArtRelease)
//  }

//  @Test
//  @OptIn(ExperimentalCoroutinesApi::class)
//  fun `get artwork error response`() = coroutineRule.runBlockingTest {
//    val entity = ReleaseEntity
//    val mbid = "mbid"
//    val mock = mock<CoverArt> {
//      onBlocking {
//        getArtwork(entity.value, mbid)
//      } doReturn Response.error(404, notFoundBody.toResponseBody())
//    }
//    val service = CoverArtService.make(mock, coroutineRule.testDispatcher)
//    val release = service.getCoverArtRelease(entity, mbid)
//    verify(mock).getArtwork(entity.value, mbid)
//    expect(release).toBeNull { "404 should return null" }
//  }

//  @Test(expected = BrainzException::class)
//  @OptIn(ExperimentalCoroutinesApi::class)
//  fun `get artwork throws`() = coroutineRule.runBlockingTest {
//    val entity = ReleaseEntity
//    val mbid = "mbid"
//    val mock = mock<CoverArt> {
//      onBlocking {
//        getArtwork(entity.value, mbid)
//      } doThrow (IllegalStateException("Bad Stuff"))
//    }
//    val service = CoverArtService.make(mock, coroutineRule.testDispatcher)
//    service.getCoverArtRelease(entity, mbid)?.let {
//      fail("getCoverArtRelease() should have thrown")
//    } ?: fail("Received null instead of exception being thrown")
//  }
}

private const val notFoundBody =
  """
{
  "error": "404",
  "help": "Not found"
}
"""
