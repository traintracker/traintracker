/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.vpavic.traintracker.interfaces.voyage.api

import io.github.vpavic.traintracker.application.VoyageFetcher
import io.github.vpavic.traintracker.domain.model.voyage.Voyage
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/{country:[a-z]{2}}/{train}"], produces = [MediaType.APPLICATION_JSON_VALUE])
class VoyageApiController {

    @GetMapping
    fun voyage(@PathVariable("country") fetcher: VoyageFetcher, @PathVariable train: String): Voyage {
        return fetcher.getVoyage(train) ?: throw VoyageNotFoundException(train)
    }
}