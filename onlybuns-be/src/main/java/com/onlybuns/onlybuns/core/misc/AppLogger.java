package com.onlybuns.onlybuns.core.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onlybuns.onlybuns.presentation.controllers.BaseController;

public class AppLogger {
    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    public void logRequest(String message, boolean success, Object... args) {
        if (success) {
            logger.info(message, args);
        } else {
            logger.error(message, args);
        }
    }
}
