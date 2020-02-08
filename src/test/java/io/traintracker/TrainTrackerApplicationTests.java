/*
 * Copyright 2020 the original author or authors.
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

package io.traintracker;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrainTrackerApplicationTests {

    @Test
    public void contextLoads() {
    }

    @TestConfiguration
    static class Config {

        @Bean
        public PostgreSQLContainer postgreSqlContainer() {
            PostgreSQLContainer postgreSqlContainer = new PostgreSQLContainer("postgres:11.5");
            postgreSqlContainer.start();
            return postgreSqlContainer;
        }

        @Bean
        public HikariDataSource dataSource(PostgreSQLContainer postgreSqlContainer) {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(postgreSqlContainer.getJdbcUrl());
            dataSource.setUsername(postgreSqlContainer.getUsername());
            dataSource.setPassword(postgreSqlContainer.getPassword());
            return dataSource;
        }

    }

}
