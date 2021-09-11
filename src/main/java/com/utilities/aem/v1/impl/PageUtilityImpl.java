package com.utilities.aem.v1.impl;

import com.day.cq.wcm.api.Page;
import com.utilities.aem.v1.PageUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.day.cq.wcm.api.NameConstants.NT_COMPONENT;

public class PageUtilityImpl implements PageUtility {


    /**
     * Given a page path, this method retrieves the count of all components grouped on their  resourceType
     *
     * @param resourceResolver
     * @param pagePath
     * @return a {Map<k,V>} of all included component resource type in a page
     */
    public Map<String, Integer> getAllComponentsCountInPage(final ResourceResolver resourceResolver,
                                                            final String pagePath) {
        if (StringUtils.isBlank(pagePath)) {
            return new HashMap<>();
        }

        Optional<Resource> pageResource = Optional.ofNullable(resourceResolver.getResource(pagePath));
        final Map<String, Integer> childComponents = new ConcurrentHashMap<>();
        pageResource.flatMap(resource -> Optional.ofNullable(resource.adaptTo(Page.class)))
                .ifPresent(page ->
                        includeSubChildElement(page.getContentResource(), childComponents));

        return childComponents;
    }

    /**
     * This helper method iterates over each resource and updates the map to include the resource type
     * or update its count.
     * This method also checks if the current resource has child and recursively call this current method for
     * each of its child element
     *
     * @param childResource
     * @param childComponents
     */
    private void getIncludedComponents(final Resource childResource, final Map<String, Integer> childComponents) {

        childComponents.computeIfPresent(childResource.getResourceType(), (resourceType, count) -> {
            childComponents.put(resourceType, count + 1);
            return null;
        });
        childComponents.computeIfAbsent(childResource.getResourceType(), (resourceType) -> {
            if (childResource.isResourceType(NT_COMPONENT)) {
                childComponents.put(resourceType, 1);
            }

            return null;
        });

        includeSubChildElement(childResource, childComponents);
    }

    /**
     * If a resource is not null, this method wll iterate the child elements and recursively call
     * the getIncludedComponents method on the resource
     *
     * @param resource
     * @param childComponents
     */
    private void includeSubChildElement(Resource resource, Map<String, Integer> childComponents) {

        Optional.ofNullable(resource).filter(Resource::hasChildren).map(resource1 -> {
            resource1.listChildren().forEachRemaining(subChildresource ->
                    getIncludedComponents(subChildresource, childComponents));
            return null;
        });

    }

    /**
     * This method verifies if a page contains a component given a component resourceType
     *
     * @param resourceResolver
     * @param pagePath
     * @param componentResourceType
     * @return a boolean indicating the presence/absence of a component
     */
    public boolean verifyComponentExistsInPage(ResourceResolver resourceResolver, String pagePath,
                                               String componentResourceType) {
        return getAllComponentsCountInPage(resourceResolver, pagePath).containsKey(componentResourceType);
    }


    /**
     * This method gets the component properties of all components in a page
     *
     * @param resourceResolver
     * @param pagePath
     * @param componentResourceType
     * @return
     */
    public Map<String, List<ValueMap>> getAllInstancesOfComponent(ResourceResolver resourceResolver, String pagePath,
                                                                  String componentResourceType) {
        if (StringUtils.isBlank(pagePath)) {
            return new HashMap<>();
        }

        Optional<Resource> pageResource = Optional.ofNullable(resourceResolver.getResource(pagePath));
        final Map<String, List<ValueMap>> componentMap = new ConcurrentHashMap<>();
        pageResource.flatMap(resource -> Optional.ofNullable(resource.adaptTo(Page.class))).ifPresent(page ->
                includeChildResourceProperties(page.getContentResource(), componentMap));
        return componentMap;
    }

    /**
     * This helper method includes the component resource type and a list of all such component properties
     * as a {@code ValueMap} object in a {@code Map<K,V>}
     *
     * @param resource-    the component resource
     * @param componentMap - a {@code Map<K,V>} of all the components resource type and
     *                     their list of properties in value object
     */
    private void getIncludedComponentProperties(final Resource resource, final Map<String, List<ValueMap>> componentMap) {

        componentMap.computeIfAbsent(resource.getResourceType(), (resourceType) -> {
            if (resource.isResourceType(NT_COMPONENT)) {
                List<ValueMap> componentPropertiesList = new ArrayList<>();
                ValueMap valueMap = resource.getValueMap();
                componentPropertiesList.add(valueMap);
                return componentPropertiesList;
            }
            return null;
        });

        componentMap.computeIfPresent(resource.getResourceType(), (resourceType, componentList) -> {
            componentList.add(resource.getValueMap());
            return componentList;
        });

        includeChildResourceProperties(resource, componentMap);
    }

    /**
     * If a resource is not null, this method wll iterate the child elements and recursively call
     * the getIncludedComponentProperties method on the resource
     *
     * @param resource
     * @param componentMap
     */
    private void includeChildResourceProperties(Resource resource, Map<String, List<ValueMap>> componentMap) {

        Optional.ofNullable(resource).filter(Resource::hasChildren)
                .map(resource1 -> {
                    resource1.listChildren()
                            .forEachRemaining(subChild -> getIncludedComponentProperties(subChild, componentMap));
                    return null;
                });
    }
}
