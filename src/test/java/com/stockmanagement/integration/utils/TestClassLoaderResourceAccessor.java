package com.stockmanagement.integration.utils;

import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Set;

/**
 * The class has the objective of adding sql files at runtime to be executed by Liquibase before integration tests
 *
 * @author frfontoura
 * @version 1.0 14/01/2020
 */
@AllArgsConstructor
public class TestClassLoaderResourceAccessor extends ClassLoaderResourceAccessor {

    private final String file;

    @Override
    public Set<String> list(final String relativeTo, final String path, final boolean includeFiles, final boolean includeDirectories, final boolean recursive) throws IOException {
        final Set<String> files = super.list(relativeTo, path, includeFiles, includeDirectories, recursive);
        if (files != null && file != null) {
            files.add(file);
        }
        return files;
    }
}
