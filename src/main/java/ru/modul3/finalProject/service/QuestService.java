package ru.modul3.finalProject.service;

import ru.modul3.finalProject.model.QuestStep;

import java.util.HashMap;
import java.util.Map;

public class QuestService {

    private final Map<String, QuestStep> steps = new HashMap<>();

    public QuestService() {
        initSteps();
    }

    private void initSteps() {
        steps.put("start", new QuestStep(
                "start",
                "Как будем добираться?",
                "На машине.",
                "Общественный транспорт.",
                "car",
                "obshTransport"
        ));

        // выбираем на чем ехать
        steps.put("car", new QuestStep(
                "car",
                "Мало топлива. Будем заправиться?",
                "Будем.",
                "Не будем. Жаль денег.",
                "continueOnCar",
                "lose"
        ));
        // на машине
        steps.put("continueOnCar", new QuestStep(
                "continueOnCar",
                "Вспоминем, что забыли права. Что делать?",
                "Возвращаемся домой.",
                "Оставляем машину. Идем на ближайшую остановку.",
                "lose",
                "obshTransport"

        ));
        // общественный транспорт
        steps.put("obshTransport", new QuestStep(
                "obshTransport",
                "Стоим на остановке и ждем. Что ждем?",
                "Прямой автобус.",
                "На перекладных",
                "directTranspont",
                "transferTranspont"
        ));
        // прямой астобус
        steps.put("directTranspont", new QuestStep(
                "directTranspont",
                "Ждем. Прямого нет. Что делаем?",
                "Возвращаемся домой.",
                "На перекладных",
                "lose",
                "transferTranspont"
        ));
        // на перекладныъ
        steps.put("transferTranspont", new QuestStep(
                "transferTranspont",
                "Едем на перекладных. Продолжаем пересаживаться?",
                "Продолжаем.",
                "Надоело. Возвращаемся домой.",
                "win",
                "lose"
        ));
        // Победа
        steps.put("win", new QuestStep(
                "win ",
                "Приехали на дачу. Это победа. УРА!",
                null,
                null,
                null,
                null
        ));

        steps.put("lose", new QuestStep(
                "lose",
                "Движение от дачи. Это поражение.",
                null,
                null,
                null,
                null
        ));
    }

    public QuestStep getSteps(String id) {
        return steps.get(id);
    }

    public boolean isFinishStep(String stepId) {
        QuestStep step = steps.get(stepId);
        return step != null && step.getOption1() != null;
    }
}
