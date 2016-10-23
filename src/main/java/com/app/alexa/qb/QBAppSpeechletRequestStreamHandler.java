package com.app.alexa.qb;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import java.util.HashSet;
import java.util.Set;

public class QBAppSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        supportedApplicationIds.add("amzn1.ask.skill.12aec33f-7e57-4ea9-a1b7-b924a36208af"); //skill arn
    }

    public QBAppSpeechletRequestStreamHandler() {
        super(new QBAppSpeechlet(), supportedApplicationIds);
    }
}