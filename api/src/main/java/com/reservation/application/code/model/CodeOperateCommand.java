package com.reservation.application.code.model;

import com.reservation.common.valid.BaseValidation;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodeOperateCommand implements BaseValidation {

  private List<CodeSaveCommand> saveCommands;
  private List<CodeUpdateCommand> updateCommands;
  private List<CodeDeleteCommand> deleteCommands;

  @Override
  public void validate() {
    saveCommands.stream().forEach(CodeSaveCommand::validate);
    updateCommands.stream().forEach(CodeUpdateCommand::validate);
    deleteCommands.stream().forEach(CodeDeleteCommand::validate);
  }
}
