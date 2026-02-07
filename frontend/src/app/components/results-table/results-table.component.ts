import { Component, ChangeDetectionStrategy, Input } from '@angular/core';
import { Incident } from '../../api.service';
import {DatePipe} from '@angular/common';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-results-table',
  templateUrl: './results-table.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    DatePipe,
    TranslatePipe
  ],
  styleUrls: ['./results-table.component.css']
})
export class ResultsTableComponent {
  @Input() incidents: Incident[] = [];
}


