/*
 * Copyright (c) 2021  Eric A. Snell
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

package com.ealva.ealvabrainz.common

@JvmInline
/**
 * The title of a Recording. May be a partial title if used in a query.
 *
 * A recording is an entity in MusicBrainz which can be linked to tracks on releases. Each track
 * must always be associated with a single recording, but a recording can be linked to any number of
 * tracks.
 */
public value class RecordingTitle(public val value: String) {
  public companion object {
    public val UNKNOWN: RecordingTitle = RecordingTitle("Unknown")
  }
}

/**
 * Convert this String to an [RecordingTitle] or [RecordingTitle.UNKNOWN] if this is null.
 */
public fun String?.toRecordingTitle(): RecordingTitle =
  if (this != null) RecordingTitle(trim()) else RecordingTitle.UNKNOWN
