package mz.co.cargo.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Model.ClienteUser;
import mz.co.cargo.Service.ClienteService;

public class GerenciarClientesController {

    @FXML private TextField searchField;
    @FXML private TableView<ClienteUser> clientesTable;
    @FXML private TableColumn<ClienteUser, String> colNome;
    @FXML private TableColumn<ClienteUser, String> colEmail;
    @FXML private TableColumn<ClienteUser, Void> colAcoes;

    private final ObservableList<ClienteUser> lista = FXCollections.observableArrayList();


    private AdminUser adminLogado;

    public void setAdminLogado(AdminUser admin) {
        this.adminLogado = admin;
    }

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));


        colAcoes.setCellFactory(new Callback<>() {
            @Override
            public TableCell<ClienteUser, Void> call(final TableColumn<ClienteUser, Void> param) {
                return new TableCell<>() {
                    private final Button btnVer = new Button("Ver");
                    private final Button btnDeletar = new Button("Deletar");
                    private final HBox pane = new HBox(5, btnVer, btnDeletar);

                    {
                        btnVer.setOnAction(event -> {
                            ClienteUser cliente = getTableView().getItems().get(getIndex());
                            onView(cliente);
                        });
                        btnDeletar.setOnAction(event -> {
                            ClienteUser cliente = getTableView().getItems().get(getIndex());
                            onDelete(cliente);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : pane);
                    }
                };
            }
        });

        // Carrega lista inicial
        lista.setAll(ClienteService.buscarTodosClientes());
        clientesTable.setItems(lista);
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String termo = searchField.getText().trim();
        if (termo.isEmpty()) {
            lista.setAll(ClienteService.buscarTodosClientes());
        } else {
            lista.setAll(ClienteService.buscarClientesPorNome(termo));
        }
    }

    private void onView(ClienteUser cliente) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Detalhes do Cliente");
        info.setHeaderText(cliente.getNome());
        info.setContentText(
                "Email: " + cliente.getEmail()

        );
        info.showAndWait();
    }

    private void onDelete(ClienteUser cliente) {
        boolean ok = ClienteService.excluirCliente(cliente.getId());
        if (ok) {
            lista.remove(cliente);
        } else {
            Alert err = new Alert(Alert.AlertType.ERROR, "Falha ao deletar cliente.");
            err.showAndWait();
        }
    }
}

