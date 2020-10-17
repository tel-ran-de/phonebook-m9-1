import {Component, TemplateRef} from '@angular/core';
import {ToastService} from "../service/toast.service";

@Component({
  selector: 'app-toasts',
  template: `
    <ngb-toast
      *ngFor="let toast of toastService.toasts"
      (hide)="toastService.remove(toast)"
      [autohide]="true"
      [class]="toast.classname"
      [delay]="toast.delay || 5000"
      [id]="toast.id || ''"
    >
      <ng-template [ngIf]="isTemplate(toast)" [ngIfElse]="text">
        <ng-template [ngTemplateOutlet]="toast.textOrTpl"></ng-template>
      </ng-template>

      <ng-template #text>{{ toast.textOrTpl }}</ng-template>
    </ngb-toast>
  `,
  host: {'[class.ngb-toasts]': 'true'}
})
export class ToastGlobalComponent {

  constructor(public toastService: ToastService) {
  }

  isTemplate(toast) {
    return toast.textOrTpl instanceof TemplateRef;
  }
}
