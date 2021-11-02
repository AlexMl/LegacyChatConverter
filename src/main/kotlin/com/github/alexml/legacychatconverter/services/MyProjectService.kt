package com.github.alexml.legacychatconverter.services

import com.intellij.openapi.project.Project
import com.github.alexml.legacychatconverter.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
