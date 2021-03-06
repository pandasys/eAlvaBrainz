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

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Disc ID
 * [“5MniTJ1axfp8JU.YZml7CRPArzc-”](https://musicbrainz.org/cdtoc/5MniTJ1axfp8JU.YZml7CRPArzc-)
 *
 * CD TOC details
 * ```
 * Full TOC:      1 4 61460 183 18098 32238 46648
 * Disc ID:       5MniTJ1axfp8JU.YZml7CRPArzc-
 * FreeDB:        21033104
 * Total tracks:  4
 * Total length:  13:39
 * ```
 * ```
 * Track details:
 * Track 	Start 	Length 	End
 * Time 	Sectors 	Time 	Sectors 	Time 	Sectors
 * 1 	0:02 	183 	3:59 	17915 	4:01 	18098
 * 2 	4:01 	18098 	3:09 	14140 	7:10 	32238
 * 3 	7:10 	32238 	3:12 	14410 	10:22 	46648
 * 4 	10:22 	46648 	3:17 	14812 	13:39 	61460
 * ```
 */
@JsonClass(generateAdapter = true)
public class Disc(
  /**
   * ID of the Disc. NOT a MusicBrainz ID (MBID)
   *
   * Disc ID is the code number which MusicBrainz uses to link a physical CD to a release listing.
   * It is a string of letters, like XzPS7vW.HPHsYemQh0HBUGr8vuU-.
   */
  public val id: String = "",
  public val sectors: Int = 0,
  public val offsets: List<Int> = emptyList(),
  @field:Json(name = "offset-count") public val offsetCount: Int = 0
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Disc

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public companion object {
    public val NullDisc: Disc = Disc(id = NullObject.ID)
    public val fallbackMapping: Pair<String, Any> = Disc::class.java.name to NullDisc
  }
}

public inline val Disc.isNullObject: Boolean
  get() = this === Disc.NullDisc
