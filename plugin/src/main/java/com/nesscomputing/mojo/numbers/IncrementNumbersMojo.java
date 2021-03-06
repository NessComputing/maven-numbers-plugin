/**
 * Copyright (C) 2011 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nesscomputing.mojo.numbers;

/**
 * Fetches the defined numbers, adds properties and increments all numbers.
 *
 * @goal inc
 */
public class IncrementNumbersMojo extends AbstractNumbersMojo
{
    /**
     * Persist the properties.
     *
     * @parameter default-value="true"
     */
    protected boolean persist = true;

    protected void doExecute() throws Exception
    {
        LOG.debug("Running GetNumbers");

        loadPropertyElements();

        if (numberFields != null) {
            for (NumberField nf : numberFields) {
                nf.increment();
            }
        }

        if (persist) {
            propertyCache.persist();
        }
    }
}
