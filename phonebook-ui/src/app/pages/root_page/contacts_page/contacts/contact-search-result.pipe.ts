import {Pipe, PipeTransform} from '@angular/core';
import {Contact} from "../../../../model/contact";

@Pipe({
  name: 'contactFilter'
})
export class ContactFilterPipe implements PipeTransform {

  transform(value: Contact[], text: string): Contact[] {
    if (!value) {
      return [];
    }
    if (!text) {
      return value;
    }
    return value.filter(value => {
      const term = text.toLowerCase();
      const contact = value.firstName + value.lastName + value.description
      return contact.toLowerCase().includes(term);
    });
  }

}
