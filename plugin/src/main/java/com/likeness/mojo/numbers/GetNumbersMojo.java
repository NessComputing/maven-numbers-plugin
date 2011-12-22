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
package com.likeness.maven.plugins.numbers;



/**
 * Fetches the defined numbers and add properties.
 *
 * @goal get
 */
public class GetNumbersMojo extends AbstractNumbersMojo
{
    /**
     * Persist the properties.
     *
     * @parameter default-value="false"
     */
    protected boolean persist = false;


    protected void doExecute() throws Exception
    {
        LOG.debug("Running GetNumbers");

        loadPropertyElements();

        if (persist) {
            // Now dump the property cache back to the files if necessary.
            propertyCache.persist();
        }
    }
}
