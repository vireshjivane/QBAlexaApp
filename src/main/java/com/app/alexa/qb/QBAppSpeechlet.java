package com.app.alexa.qb;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.util.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import java.nio.charset.Charset;

public class QBAppSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(QBAppSpeechlet.class);

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

       if ("QIntent".equals(intentName)) {
            return getQResponse();
        }
        else if ("GithubIntent".equals(intentName)) {
            return getGithubResponse(request.getIntent());
        }
        else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Welcome to the Alexa Skills Kit, you can say hello";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Welcome");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getQResponse() {

        String speechText = "";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("QuickBooks");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "You can say hello to me!";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Help");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }


    private SpeechletResponse getGithubResponse(Intent intent) {

        String name;

        Slot itemSlot = intent.getSlot("user");
        if (itemSlot != null && itemSlot.getValue() != null) {
             name = itemSlot.getValue();

        }else
        {
            return getHelpResponse();
        }

        String speechText = "The result for " + name;

        try {
            JSONObject json = new JSONObject(IOUtils.toString(new URL("https://api.github.com/users/" + name), Charset.forName("UTF-8")));
            speechText += json.get("name");
        }catch (Exception e){
            e.printStackTrace();
        }

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Github");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

}
