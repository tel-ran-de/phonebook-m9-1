import {Pipe, PipeTransform} from '@angular/core';
import {Contact} from "../../../../model/contact";

@Pipe({
  name: 'contactFilter'
})
export class ContactFilterPipe implements PipeTransform {

  transform(value: Contact[], searchTerm: string): Contact[] {
    if (!value)
      return [];

    if (!searchTerm)
      return value;

    return value.filter(value => {
      const term = searchTerm.toLowerCase();
      const contact = value.firstName + value.lastName + value.description
      return contact.toLowerCase().includes(term);
    });
  }
}
