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

import com.ealva.ealvabrainz.common.Formatting.toIso
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

public fun LocalDate.brainzFormat(): String = toIso.format(this)

public fun Date.brainzFormat(): String = toIso.format(
  toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDate()
)

public fun Date.toLocalDate(): LocalDate = toInstant()
  .atZone(ZoneId.systemDefault())
  .toLocalDate()

public fun String.isoToLocalDate(): LocalDate = LocalDate.parse(this, toIso)

public object Formatting {
  public val toIso: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
  public val toYear: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy")
}
