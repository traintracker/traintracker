/*
 * Copyright 2019 the original author or authors.
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

package io.traintracker.interfaces.home

import io.traintracker.application.VoyageFetcher
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping(produces = [MediaType.TEXT_HTML_VALUE])
class HomeWebController {
    @GetMapping(path = ["/"])
    fun home(@ModelAttribute("countries") countries: Set<String>): String {
        return "redirect:/" + countries.iterator().next() + "/"
    }

    @GetMapping(path = ["/{country:[a-z]{2}}"])
    fun country(@PathVariable country: String): String {
        return "redirect:/$country/"
    }

    @GetMapping(path = ["/{country:[a-z]{2}}/"])
    fun country(@PathVariable("country") fetcher: VoyageFetcher?, model: Model): String {
        if (fetcher == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        model.addAttribute("country", fetcher.getCountry())
        return "home"
    }
}
