package dev.tsnanh.vku.views.my_vku.teacher_evaluation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.tsnanh.vku.domain.entities.Resource;
import dev.tsnanh.vku.domain.entities.Teacher;
import dev.tsnanh.vku.domain.usecases.RetrieveTeachersUseCase;
import dev.tsnanh.vku.utils.SecretConstants;
import kotlin.Lazy;

import static org.koin.java.KoinJavaComponent.inject;

public class TeacherEvaluationViewModel extends ViewModel {
    private Lazy<RetrieveTeachersUseCase> useCase = inject(RetrieveTeachersUseCase.class);
    public LiveData<Resource<List<Teacher>>> teachers = useCase.getValue().getTeachersLiveData(SecretConstants.TEACHERS_URL);
}
