import { Component, Output, EventEmitter } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  imports: [
    ReactiveFormsModule,
    TranslatePipe
  ],
  styleUrls: ['./filter.component.css']
})
export class FilterComponent {
  filterForm: FormGroup;

  @Output() search = new EventEmitter<{ title?: string; description?: string; severity?: string; owner?: string }>();

  constructor(private fb: FormBuilder) {
    this.filterForm = this.fb.group({
      title: [''],
      description: [''],
      severity: [''],
      owner: ['']
    });
  }

  onSearch() {
    this.search.emit(this.filterForm.value);
  }
}
