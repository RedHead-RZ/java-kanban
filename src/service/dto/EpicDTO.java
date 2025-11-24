package service.dto;

import enums.Status;
import model.Epic;

public class EpicDTO {
    private Integer id;
    private String label;
    private String description;
    private Status status;

    public Epic toEpic() {
        return new Epic(label, description);
    }

    public static EpicDTO toEpicDTO(Epic epic) {
        EpicDTO epicDTO = new EpicDTO();
        epicDTO.id = epic.getId();
        epicDTO.label = epic.getLabel();
        epicDTO.description = epic.getDescription();
        epicDTO.status = epic.getStatus();
        return epicDTO;
    }
}
