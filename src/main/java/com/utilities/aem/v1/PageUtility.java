package com.utilities.aem.v1;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import java.util.List;
import java.util.Map;

/**
 * Static utilities for AEM Pages
 */
public interface PageUtility {

    /**
     * This method would retrieve all the direct child and sub-child of the direct components of the page
     *
     * @param resourceResolver
     * @param pagePath
     * @return a {@code Map<K,V>} of all the component resource types and its occurrence in a page
     */
    Map<String, Integer> getAllComponentsCountInPage(ResourceResolver resourceResolver, String pagePath);

    /**
     * @param resourceResolver
     * @param pagePath
     * @param componentResourceType
     * @return
     */

    boolean verifyComponentExistsInPage(ResourceResolver resourceResolver, String pagePath,
                                        String componentResourceType);

    /**
     * This method retrieves properties of all the instances of a component in a page
     *
     * @param resourceResolver
     * @param pagePath
     * @param componentResourceType
     * @return a {@code Map<K,V>} of all component resourceType as key and their corresponding list of {@code ValueMap}
     * objects as properties in the value object
     */
    Map<String, List<ValueMap>> getAllInstancesOfComponent(ResourceResolver resourceResolver, String pagePath,
                                                           String componentResourceType);
}
