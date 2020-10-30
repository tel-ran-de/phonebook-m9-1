import {Pipe, PipeTransform} from '@angular/core';
import {Email} from "../../../../model/email";

@Pipe({
  name: 'emailFilter'
})
export class EmailFilterPipe implements PipeTransform {

  transform(value: Email[], searchTerm: string): Email[] {
    if (!value)
      return [];

    if (!searchTerm)
      return value;

    return value.filter(emailItem => {
      const term = searchTerm.toLowerCase();
      return emailItem.email.toLowerCase().includes(term);
    });
  }
}
