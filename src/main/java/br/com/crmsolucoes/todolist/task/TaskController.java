package br.com.crmsolucoes.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.crmsolucoes.todolist.utils.Utils;
import jakarta.servlet.FilterRegistration.Dynamic;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired 
    private ITaskRepository taskRepository;

    @PostMapping("")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {

        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("A Data de Início e a Data de Conclusão devem ser maiores que a Data Atual");
        }

        if (taskModel.getTitle().length() > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("O Título da Tarefa não pode ter mais de 50 caracteres");
        }

        var taskCreated = this.taskRepository.save(taskModel);

        return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
    }

    @GetMapping("")
    public List<TaskModel> list(HttpServletRequest request) {
        return this.taskRepository.findByIdUser((UUID) request.getAttribute("idUser"));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {

        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Tarefa não encontrada");
        }

        if (!task.getIdUser().equals(request.getAttribute("idUser"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Usuário não tem Permissão para alterar esta tarefa");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }
}