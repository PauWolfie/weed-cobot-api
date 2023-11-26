package com.sacavix.telegramboot.service;

import com.google.cloud.firestore.*;
import com.google.firebase.database.annotations.Nullable;
import com.sacavix.telegramboot.dto.UserDTO;
import com.sacavix.telegramboot.model.Plant;
import com.sacavix.telegramboot.model.User;
import com.sacavix.telegramboot.service.api.PlantServiceAPI;
import com.sacavix.telegramboot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TelegramBoot extends TelegramLongPollingBot {
    // Firestore
    private Firestore firestore;
    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        firestore = FirestoreOptions.getDefaultInstance().getService();
        executorService = Executors.newFixedThreadPool(5);
    }

    @Autowired
    private UserServiceImpl userServiceAPI;

    @Autowired
    private PlantServiceAPI plantServiceAPI;

    @Override
    public void onUpdateReceived(Update update) {
        final String messageTextReceived = update.getMessage().getText();

        // Se obtiene el id de chat del usuario
        final long chatId = update.getMessage().getChatId();

        // Se crea un objeto mensaje
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        if (messageTextReceived.equals("/start")) {
            message.setText("Hi! I'm your plant! Send me your user id " +
                    "to stay up to date of all my stats!");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            message.setText("You can get your user id in https://weedcobot.web.app/");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;

        }

        if(messageTextReceived.equals("/stop")){
            message.setText("Realtime notifications stopped! See you soon!");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            executorService.shutdown();
            return;
        }

        // Get user
        try {
            UserDTO u = userServiceAPI.get(String.valueOf(messageTextReceived));
            if (u == null) {
                message.setText("Hi! I'm your plant! Send me your user id please!");
            } else {
                u.setChatId(String.valueOf(chatId));
                userServiceAPI.save(u, u.getId());
                message.setText("Hi " + u.getName() + "!");
                execute(message);

                message.setText("Your will recive real time update of your plants!");
                execute(message);

                message.setText("Your plants are: ");
                execute(message);

                ArrayList<String> plants = u.getPlants();
                for (String plant : plants) {
                    message.setText(plantServiceAPI.get(plant).getId());
                    execute(message);
                    executorService.submit(() -> {
                        try {
                            observePlantValue(plant, chatId, u);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "weed_cobot_bot";
    }

    @Override
    public String getBotToken() {
        return "6951303306:AAFP98bVg4SNYHMkBnuVSjWy1CuCNmQEHGg";
    }

    private void observePlantValue(String plantId, long chatId, UserDTO user) throws Exception {
        System.out.println("Created thread for: " + plantId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        // Código para observar el valor en Firestore para la planta con plantId
        DocumentReference plantDocRef = firestore.collection("plantes").document("P_01_EPS");
        while (true) {
            Plant p = plantServiceAPI.get(plantId);

            if (user.getMax_air_humidity() < p.getAir_humidity()) {
                message.setText("Message from " + plantId + ":\n ☀\uFE0F I need a drier environment! \n\n" +
                        "Actual humidity: " + p.getAir_humidity() + "\n" + "Max humidity: " +
                        user.getMax_air_humidity());
                execute(message);

                // Send gif
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(String.valueOf(chatId));
                InputFile inputFile = new InputFile(new File("./assets/humidity.gif"));
                sendDocument.setDocument(inputFile);

                try {
                    execute(sendDocument);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            if (user.getMin_air_humidity() > p.getAir_humidity()) {
                message.setText("Message from " + plantId + ":\n \n" +
                        "\uD83C\uDF2B\uFE0F I need a more humid environment! \n\n" +
                        "Actual humidity: " + p.getAir_humidity() + "\n" + "Min humidity: " +
                        user.getMin_air_humidity());
                execute(message);

                // Send gif
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(String.valueOf(chatId));
                InputFile inputFile = new InputFile(new File("./assets/dry.gif"));
                sendDocument.setDocument(inputFile);

                try {
                    execute(sendDocument);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            if (user.getMax_soil_humidity() < p.getSoil_humidity()) {
                message.setText("Message from " + plantId + ":\n \n" +
                        "\uD83D\uDC80 Bitch I'm dehydrated. Water me! \n\n" +
                        "Actual humidity: " + p.getSoil_humidity() + "\n" + "Min humidity: " +
                        user.getMax_soil_humidity());
                execute(message);

                // Send gif
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(String.valueOf(chatId));
                InputFile inputFile = new InputFile(new File("./assets/dehidrated.gif"));
                sendDocument.setDocument(inputFile);

                try {
                    execute(sendDocument);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            if (user.getMin_soil_humidity() > p.getSoil_humidity()) {
                message.setText("Message from " + plantId + ":\n \n" +
                        "\uD83C\uDFCA\u200D♀\uFE0F I'm drowning in juices. Help me! \n\n" +
                        "Actual humidity: " + p.getSoil_humidity() + "\n" + "Max humidity: " +
                        user.getMax_soil_humidity());
                execute(message);

                // Send gif
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(String.valueOf(chatId));
                InputFile inputFile = new InputFile(new File("./assets/drowning.gif"));
                sendDocument.setDocument(inputFile);

                try {
                    execute(sendDocument);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            if (user.getMax_temperature() < p.getTemperature()) {
                message.setText("Message from " + plantId + ":\n \uD83D\uDD25 I'm burning in hell. Jesus save me! \n\n" +
                        "Actual temp: " + p.getTemperature() + "\n" + "Max temp: " + user.getMax_temperature());
                execute(message);

                // Send gif
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(String.valueOf(chatId));
                InputFile inputFile = new InputFile(new File("./assets/fire.gif"));
                sendDocument.setDocument(inputFile);

                try {
                    execute(sendDocument);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            if (user.getMin_temperature() > p.getTemperature()) {
                message.setText("Message from " + plantId + ":\n \n" +
                        "\uD83E\uDD76 I'm freezing my balls off. Heat me up! \n\n" +
                        "Actual temperature: " + p.getTemperature() + "\n" + "Min temperature: " +
                        user.getMin_temperature());
                execute(message);

                // Send gif
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(String.valueOf(chatId));
                InputFile inputFile = new InputFile(new File("./assets/freezed.gif"));
                sendDocument.setDocument(inputFile);

                try {
                    execute(sendDocument);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            // Espera 5 segundos
            Thread.sleep(5000);
        }
    }
}
