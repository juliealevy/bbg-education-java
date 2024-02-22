package com.play.java.bbgeducation.application.courses.getall;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseGetAllCommand implements Command<List<CourseResult>> {
}
