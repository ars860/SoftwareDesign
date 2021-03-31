package manage.command.model;

public class CreateUserCommand implements Command {
    String name;

    public CreateUserCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
