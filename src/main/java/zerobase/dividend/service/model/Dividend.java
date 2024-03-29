package zerobase.dividend.service.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Dividend {
    private LocalDateTime data;
    private String dividend;
}
