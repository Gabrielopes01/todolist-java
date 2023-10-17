package br.com.crmsolucoes.todolist.user;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;


/**
 * Modificador
 * - public
 * - private
 * - protected
 */
@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired //Gerencia os Repositories
    private IUserRepository userRepository;

    /**
     * String (Texto)
     * Integer (Inteiros)
     * Double (Numero Decimal)
     * Float (Numero Decimal com menos casas decimais)
     * char (A, B, C)
     * Date (Data)
     * Object (Objeto)
     * Void (Sem Retorno)
     */
    @PostMapping("")
    public ResponseEntity create(@RequestBody UserModel userModel) {

    //Possivel Metodo Alternativo para Updates
        // userModel.setUsername("Teste");
        // System.out.println(userModel.getUsername());


        //     try {
        //         Method[] methods = userModel.getClass().getDeclaredMethods();

        //         for (Method m : methods) {
                   
        //             if (m.getName() == "setUsername") {
        //                 m.invoke(userModel, "Jeff");
        //             } else {
        //                 System.out.println(m.getName());
        //             }
        //         }
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     } 

        // System.out.println(userModel.getUsername());

        if(this.userRepository.findByUsername(userModel.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já Existe.");
        }

        var passwordHash = BCrypt.withDefaults()
                                 .hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHash);
        
        var userCreated = this.userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
