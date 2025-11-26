package service.dto;

import enums.Status;
import manager.TaskManager;
import model.Epic;
import model.Subtask;

public class SubtaskDTO {
    private Integer id;
    private String label;
    private String description;
    private Status status;
    private int parentEpicId;

    public Subtask toSubtask(TaskManager manager) {
        Epic parentEpic = (Epic) manager.getTaskById(parentEpicId);
        if (parentEpic == null) {
            throw new IllegalArgumentException("Epic not found with id: " + parentEpicId);
        }

        Subtask subtask = new Subtask(label, description, parentEpic);
        if (id != null) subtask.setId(id);
        if (status != null) subtask.setStatus(status);

        return subtask;
    }

    public static SubtaskDTO toSubtaskDTO(Subtask subtask) {
        SubtaskDTO subtaskDTO = new SubtaskDTO();
        subtaskDTO.id = subtask.getId();
        subtaskDTO.label = subtask.getLabel();
        subtaskDTO.description = subtask.getDescription();
        subtaskDTO.status = subtask.getStatus();
        subtaskDTO.parentEpicId = subtask.getParentTask().getId();
        return subtaskDTO;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ",\"label\":\"" + label +
                "\",\"description\":\"" + description +
                "\",\"parentEpicId\":" + parentEpicId +
                "}";
    }

}