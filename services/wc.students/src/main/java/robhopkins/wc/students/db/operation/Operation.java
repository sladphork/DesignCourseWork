package robhopkins.wc.students.db.operation;

import robhopkins.wc.students.exception.ServerException;

public interface Operation<RESULT> {

    RESULT execute() throws ServerException;
}
