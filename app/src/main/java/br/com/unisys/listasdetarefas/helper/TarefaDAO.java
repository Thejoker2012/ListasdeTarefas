package br.com.unisys.listasdetarefas.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.unisys.listasdetarefas.model.Tarefa;

public class TarefaDAO implements ITarefaDAO{

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public TarefaDAO(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put("nome",tarefa.getNomeTarefa());

        try {
            Log.i("INFO","Tarefa Salva Com Sucesso!");
            escreve.insert(DbHelper.TABELA_TAREFAS,null,cv);
        }catch (Exception e){
            Log.i("INFO","Erro ao salvar tarefa" +e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put("nome",tarefa.getNomeTarefa());
        try {
            String [] args = {tarefa.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS,cv,"id=?",args );
            Log.i("INFO","Tarefa Atualizada Com Sucesso!");
        }catch (Exception e){
            Log.i("INFO","Erro ao Atualizar tarefa" +e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deletar(Tarefa tarefa) {

        try {
            String [] args = {tarefa.getId().toString()};
            escreve.delete(DbHelper.TABELA_TAREFAS,"id=?",args );
            Log.i("INFO","Tarefa Removida Com Sucesso!");
        }catch (Exception e){
            Log.i("INFO","Erro ao Remover a Tarefa" +e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Tarefa> listar() {
        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT  * FROM " + DbHelper.TABELA_TAREFAS + " ;";
        Cursor c = le.rawQuery(sql, null);

        while (c.moveToNext()){

            Long id = c.getLong(c.getColumnIndex("id") );
            String nomeTarefa = c.getString(c.getColumnIndex("nome"));

            Tarefa tarefa = new Tarefa();
            tarefa.setId(id);
            tarefa.setNomeTarefa(nomeTarefa);

            tarefas.add(tarefa);
        }
        return  tarefas;
    }
}
